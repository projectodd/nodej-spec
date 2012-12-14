package org.projectodd.nodej.spec;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.runner.RunWith;

@RunWith(NodeJSpecRunner.class)
public class NodeJTestHelper {

	private String folderName;

	public NodeJTestHelper(String folderName) {
		this.folderName = folderName;
	}

    public boolean shouldRun(File file) {
        return true;
    }
    
	public Collection<File> files() throws URISyntaxException {
		return NodeJTestHelper.listFiles(this.folderName);
	}
    
    public static Collection<File> listFiles(String folderName) throws URISyntaxException {
        final URL resource = NodeJTestHelper.class.getResource("/suite");
        if (resource.getProtocol().equals("file")) {
            File suiteDir = new File(resource.toURI());
            File folder = new File(suiteDir, folderName);
            if (!folder.exists()) {
                throw new RuntimeException(String.format(
                        "Folder %s doesn't exist in /suite.", folderName));
            }

            return FileUtils.listFiles(folder, new SuffixFileFilter("js"), TrueFileFilter.INSTANCE);
        }

        throw new RuntimeException("failed loading test suite");
    }
}
