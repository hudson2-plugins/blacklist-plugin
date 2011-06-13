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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hudsonci.plugins.blacklist.BlacklistedFeature;

/**
 * Utilities to index a collection of {@link BlacklistedFeature}s.
 * 
 * @author Jamie Whitehouse
 */
@Named
@Singleton
class FeatureIndexer
{
    Map<String, BlacklistedFeature> mapFeaturesToSimpleName( final List<BlacklistedFeature> features )
    {
        assert features != null;
        HashMap<String, BlacklistedFeature> map = new HashMap<String, BlacklistedFeature>();
        for ( BlacklistedFeature feature : features )
        {
            if ( feature != null )
            {
                map.put( feature.getClass().getSimpleName(), feature );
            }
        }
        return Collections.unmodifiableMap( map );
    }
}
