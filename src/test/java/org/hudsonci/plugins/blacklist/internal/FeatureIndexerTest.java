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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hudsonci.plugins.blacklist.BlacklistedFeature;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link FeatureIndexer}.
 */
public class FeatureIndexerTest
{
    private final FeatureIndexer featureIndexer = new FeatureIndexer();
    
    /**
     * Users of internal classes should behave hence don't need to be defensive.
     */
    @Test
    @Ignore
    public void nullListDoesNotThrowException()
    {
        featureIndexer.mapFeaturesToSimpleName( null );
    }

    @Test
    public void shortnameIsUsedForKey()
    {
        List<BlacklistedFeature> features = new ArrayList<BlacklistedFeature>();
        features.add( new TestBlacklistedFeature() );

        Map<String, BlacklistedFeature> mappedFeatures = featureIndexer.mapFeaturesToSimpleName( features );
        assertThat( mappedFeatures, hasKey( TestBlacklistedFeature.class.getSimpleName() ) );
    }

    @Test
    public void nullListElementsShouldNotThrowException()
    {
        List<BlacklistedFeature> features = new ArrayList<BlacklistedFeature>();
        features.add( null );

        featureIndexer.mapFeaturesToSimpleName( features );
    }

    @Test
    public void duplicateListElementsShouldProduceOneMapEntry()
    {
        List<BlacklistedFeature> features = new ArrayList<BlacklistedFeature>();
        features.add( new TestBlacklistedFeature() );
        features.add( new TestBlacklistedFeature() );

        Map<String, BlacklistedFeature> mappedFeatures = featureIndexer.mapFeaturesToSimpleName( features );
        assertThat( mappedFeatures.entrySet(), hasSize( 1 ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void mapShouldBeImmutable()
    {
        List<BlacklistedFeature> features = Collections.emptyList();
        featureIndexer.mapFeaturesToSimpleName( features ).put( "test", new TestBlacklistedFeature() );
    }

    private class TestBlacklistedFeature
        implements BlacklistedFeature
    {
        public void enable()
        {
            throw new UnsupportedOperationException( "Not implemented in test object" );
        }

        public void disable()
        {
            throw new UnsupportedOperationException( "Not implemented in test object" );
        }
    }
}
