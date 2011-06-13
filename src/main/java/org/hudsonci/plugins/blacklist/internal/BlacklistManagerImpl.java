/**
 * The MIT License
 *
 * Copyright (c) 2010-2011 Sonatype, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.hudsonci.plugins.blacklist.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hudsonci.plugins.blacklist.BlacklistManager;
import org.hudsonci.plugins.blacklist.BlacklistedFeature;

/**
 * Default {@link BlacklistManager} implementation that uses the classes
 * simple name to locate individual features.
 * Logs operations and provides additional methods for testing and
 * debugging.
 * 
 * @author Jamie Whitehouse
 * @since 1.1
 */
@Named
@Singleton
public class BlacklistManagerImpl
    implements BlacklistManager
{
    private static final Logger log = LoggerFactory.getLogger( BlacklistManager.class );

    private final Map<String, BlacklistedFeature> features;

    @Inject
    public BlacklistManagerImpl( final List<BlacklistedFeature> features, final FeatureIndexer indexer )
    {
        assert features != null;
        this.features = indexer.mapFeaturesToSimpleName( features );
    }

    /**
     * Retrieves the features managed by this class. Useful in testing and debugging.
     * 
     * @return the features managed by this class
     */
    public Collection<BlacklistedFeature> getManagedFeatures()
    {
        // The indexer creates an immutable map so it's safe to return the
        // values as is.
        return features.values();
    }

    public void disableAllFeatures()
    {
        for ( BlacklistedFeature feature : features.values() )
        {
            disable( feature );
        }
    }

    public void enableAllFeatures()
    {
        for ( BlacklistedFeature feature : features.values() )
        {
            enable( feature );
        }
    }

    public void disableFeature( String featureName )
    {
        BlacklistedFeature feature = features.get( featureName );
        if ( feature != null )
        {
            disable( feature );
        }
    }

    public void enableFeature( String featureName )
    {
        BlacklistedFeature feature = features.get( featureName );
        if ( feature != null )
        {
            enable( feature );
        }
    }

    private void disable( BlacklistedFeature feature )
    {
        log.debug( "Disabling feature {}", feature.getClass().getSimpleName() );
        feature.disable();
    }

    private void enable( BlacklistedFeature feature )
    {
        log.debug( "Enabling feature {}", feature.getClass().getSimpleName() );
        feature.enable();
    }
}
