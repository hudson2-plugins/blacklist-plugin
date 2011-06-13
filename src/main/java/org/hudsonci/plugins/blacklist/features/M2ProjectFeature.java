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

package org.hudsonci.plugins.blacklist.features;

import org.hudsonci.plugins.blacklist.internal.DescriptorLocator;
import hudson.DescriptorExtensionList;
import hudson.Functions;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hudsonci.plugins.blacklist.BlacklistedFeature;

/**
 * Disables creation of new "maven2 project" types.
 * 
 * Existing projects will still be visible and executable.
 * 
 * @author Jamie Whitehouse
 */
@Named
@Singleton
class M2ProjectFeature
    implements BlacklistedFeature
{
    private static final String HUDSON_M2_JOB_TYPE = "hudson.maven.MavenModuleSet";

    private final DescriptorLocator locator;

    @Inject
    public M2ProjectFeature( final DescriptorLocator locator )
    {
        this.locator = locator;
    }

    public void disable()
    {
        // To remove from the 'new job' view it needs to be removed from the TopLevelItemDescriptor list.
        // It is not enough to remove the descriptor from the extension list of descriptors.
        // In other words, this does not work: Hudson.getInstance().getExtensionList( Descriptor.class ).remove(
        // bannedExtension );
        DescriptorExtensionList<TopLevelItem, Descriptor<TopLevelItem>> topLevelItemDescriptors =
            getTopLevelItemDescriptors();
        // Force loading of descriptors, at least until the type is found.
        Descriptor<TopLevelItem> bannedExtension = topLevelItemDescriptors.findByName( HUDSON_M2_JOB_TYPE );

        // If the extension is available
        // remove it from the top level items
        // preventing it from being shown on the new job page.
        if ( bannedExtension != null )
        {
            topLevelItemDescriptors.remove( bannedExtension );

            Functions.getGlobalConfigIgnoredDescriptors().add(bannedExtension.getClass().getName());
        }
    }

    // Don't use parameterized type since it would force a dependency on the
    // MavenModuleSet class which is provided by a plugin that may be removed
    // in some installations.
    @SuppressWarnings( { "rawtypes", "deprecation", "unchecked" } )
    public void enable()
    {
        Descriptor extenstionToEnable = locator.getDescriptor( HUDSON_M2_JOB_TYPE );

        DescriptorExtensionList<TopLevelItem, Descriptor<TopLevelItem>> topLevelItemDescriptors =
            getTopLevelItemDescriptors();

        Descriptor<TopLevelItem> previouslyRemovedExtension = topLevelItemDescriptors.findByName( HUDSON_M2_JOB_TYPE );

        // If the extension is available AND is not present in the top level items
        // add it to the top level items
        // to show it on the new job page.
        if ( extenstionToEnable != null && previouslyRemovedExtension == null )
        {
            topLevelItemDescriptors.add( extenstionToEnable );

            Functions.getGlobalConfigIgnoredDescriptors().remove(extenstionToEnable.getClass().getName());
        }
    }

    private DescriptorExtensionList<TopLevelItem, Descriptor<TopLevelItem>> getTopLevelItemDescriptors()
    {
        return locator.getDescriptorList( TopLevelItem.class );
    }
}
