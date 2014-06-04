package org.modeshape.example.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.modeshape.common.util.IoUtil;
import org.modeshape.jcr.api.JcrTools;

/**
 * Performs various operations on a predefined repository.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class RepositoryService {
    private static final JcrTools TOOLS = new JcrTools();
    private static final Repository REPOSITORY = RepositoryProvider.getRepository();

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();
    private static final int BINARY_LENGTH = 2048;
    private static final String HEADER = "this is a text file ";

    public void storeFilesAndFolders(int foldersCount, int filesCount) throws RepositoryException, IOException {
        if (foldersCount <= 0) {
            throw new IllegalArgumentException("The folders count must be a positive integer");
        }
        for (int i = 0; i < foldersCount; i++) {
            Session session = REPOSITORY.login();
            long suffix = session.getRootNode().getNodes().getSize();
            String folderName = "folder_" + suffix;
            try {
                for (int j = 0; j < filesCount; j++) {
                    String fileName = "file_" + j;
                    TOOLS.uploadFile(session, "/" + folderName + "/" + fileName, randomBinary());
                }
                session.save();
            } finally {
                session.logout();
            }
        }
    }

    private InputStream randomBinary() {
        return new ByteArrayInputStream(randomString(BINARY_LENGTH).getBytes());
    }

    private static String randomString(int length) {
        StringBuilder rndStringBuilder = new StringBuilder(length);
        rndStringBuilder.append(HEADER);
        for (int i = 0; i < length - HEADER.length(); i++) {
            rndStringBuilder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return rndStringBuilder.toString();
    }

    public List<String> listFolders() throws Exception {
        List<String> folders = new ArrayList<String>();
        Session session = REPOSITORY.login();
        StringBuilder builder = new StringBuilder();

        try {
            NodeIterator nodes = session.getRootNode().getNodes();
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                if (!node.isNodeType("nt:folder")) {
                    continue;
                }
                builder.append(node.getPath()).append("[");
                NodeIterator files = node.getNodes();
                while (files.hasNext()) {
                    Node file = files.nextNode();
                    builder.append(file.getName());
                    Binary content = file.getNode("jcr:content").getProperty("jcr:data").getBinary();
                    byte[] actualContentBytes = IoUtil.readBytes(content.getStream());
                    String actualContent = new String(actualContentBytes);
                    boolean isValidContent = actualContent.length() == BINARY_LENGTH && actualContent.startsWith(HEADER);
                    if (isValidContent) {
                        builder.append( "(valid content) ");
                    } else {
                        builder.append(" (invalid content) ");
                    }

                    if (files.hasNext()) {
                        builder.append(",");
                    }
                }
                builder.append("]");
                folders.add(builder.toString());
                builder.delete(0, builder.length());
            }
            return folders;
        } finally {
            session.logout();
        }
    }

    public String repositoryName() {
        return REPOSITORY.getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
    }
}
