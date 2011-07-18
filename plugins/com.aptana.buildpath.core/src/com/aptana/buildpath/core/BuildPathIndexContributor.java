package com.aptana.buildpath.core;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.aptana.core.logging.IdeLog;
import com.aptana.index.core.IIndexFileContributor;

public class BuildPathIndexContributor implements IIndexFileContributor
{
	/*
	 * (non-Javadoc)
	 * @see com.aptana.index.core.IIndexFileContributor#contributeFiles(java.net.URI)
	 */
	public Set<IFileStore> contributeFiles(URI containerURI)
	{
		IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(containerURI);
		Set<IFileStore> result = new HashSet<IFileStore>();

		if (containers != null && containers.length > 0)
		{
			for (IContainer container : containers)
			{
				if (container instanceof IProject)
				{
					IProject project = (IProject) container;
					Set<BuildPathEntry> entries = BuildPathManager.getInstance().getBuildPaths(project);

					if (entries != null)
					{
						for (BuildPathEntry entry : entries)
						{
							try
							{
								IFileStore fileStore = EFS.getStore(entry.getPath());

								result.add(fileStore);
							}
							catch (CoreException e)
							{
								IdeLog.logError(BuildPathCorePlugin.getDefault(), e.getMessage(), e);
							}
						}
					}
				}
			}
		}

		return result;
	}
}
