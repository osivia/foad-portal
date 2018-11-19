package fr.gouv.education.foad.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Component;

import fr.gouv.education.foad.portlet.model.FolderTask;

/**
 * Folder task comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see FolderTask
 */
@Component
public class FolderTaskComparator implements Comparator<FolderTask> {

    /**
     * Constructor.
     */
    public FolderTaskComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(FolderTask folder1, FolderTask folder2) {
        int result;

        if (!BooleanUtils.xor(new boolean[]{folder1.isFolder(), folder2.isFolder()})) {
            result = folder1.getDisplayName().compareToIgnoreCase(folder2.getDisplayName());
        } else if (folder1.isFolder()) {
            result = -1;
        } else {
            result = 1;
        }

        return result;
    }

}
