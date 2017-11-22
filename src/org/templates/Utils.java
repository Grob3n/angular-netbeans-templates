package org.templates;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

public class Utils {

	public static Set<FileObject> createFilesFromTemplate(WizardDescriptor wizard, String type) throws IOException {

		Set<FileObject> createdFiles = new HashSet<>();

		// FreeMarker Template will get its variables from HashMap.
		// HashMap key is the variable name.
		Map args = new HashMap();

		//Get the package:
		FileObject dir = Templates.getTargetFolder(wizard);

		//Get the class:
		String target = Templates.getTargetName(wizard);

		String sFolder = FileUtil.getFileDisplayName(dir);
		sFolder = sFolder.replace('\\', '/');
		if (sFolder.contains("webapp/")) {
			sFolder = sFolder.substring(sFolder.indexOf("webapp/") + "webapp/".length());
		}
		boolean createFolder = (boolean) wizard.getProperty("createFolder");
		DataFolder df;

		args.put("name", target);
		if (createFolder) {
			args.put("path", sFolder + "/" + target);
			FileObject targetFolder = dir.createFolder(target);
			df = DataFolder.findFolder(targetFolder);
		} else {
			args.put("path", sFolder);
			df = DataFolder.findFolder(dir);
		}

		FileObject[] templates = Templates.getTemplate(wizard).getParent().getChildren();

		for (FileObject t : templates) {
			if (t.getName().equals("angular" + type)) {
				DataObject dTemplate = DataObject.find(t);
				//Define the template from the above,
				//passing the package, the file name, and the map of strings to the template:
				DataObject dobj = dTemplate.createFromTemplate(df, target, args);

				//Obtain a FileObject:
				createdFiles.add(dobj.getPrimaryFile());
			}
		}

		return createdFiles;
	}
}
