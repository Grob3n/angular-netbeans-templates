/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.templates.service;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@TemplateRegistration(
		folder = "Angular Templates",
		content = "templates/angularService.js",
		displayName = "#AngularServiceWizardIterator_displayName",
		description = "description.html",
		scriptEngine = "freemarker")
@Messages("AngularServiceWizardIterator_displayName=Angular Service")
public final class AngularServiceWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

	private int index;

	private WizardDescriptor wizard;
	private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

	private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
		if (panels == null) {
			panels = new ArrayList<>();
			Project p = Templates.getProject(wizard);
			SourceGroup[] groups = ProjectUtils.getSources(p).getSourceGroups(Sources.TYPE_GENERIC);

			WizardDescriptor.Panel<WizardDescriptor> advNewFilePanel = Templates.buildSimpleTargetChooser(p, groups).create();
			panels.add(advNewFilePanel);

			String[] steps = createSteps();
			for (int i = 0; i < panels.size(); i++) {
				Component c = panels.get(i).getComponent();
				if (steps[i] == null) {
					// Default step name to component name of panel. Mainly
					// useful for getting the name of the target chooser to
					// appear in the list of steps.
					steps[i] = c.getName();
				}
				if (c instanceof JComponent) { // assume Swing components
					JComponent jc = (JComponent) c;
					jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
					jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
					jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
					jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
					jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
				}
			}
		}
		return panels;
	}

	@Override
	public Set<?> instantiate() throws IOException {

		Set<FileObject> createdFiles = new HashSet<>();

		// FreeMarker Template will get its variables from HashMap.
		// HashMap key is the variable name.
		Map args = new HashMap();

		//Get the template and convert it:
		FileObject template = Templates.getTemplate(wizard);
		DataObject dTemplate = DataObject.find(template);

		//Get the package:
		FileObject dir = Templates.getTargetFolder(wizard);

		//Get the class:
		String target = Templates.getTargetName(wizard);
		args.put("name", target);
//		FileObject targetFolder = dir.createFolder(target);
		DataFolder df = DataFolder.findFolder(dir);

		FileObject[] templates = Templates.getTemplate(wizard).getParent().getChildren();

		for (FileObject t : templates) {
			if (t.getName().startsWith("angularService")) {
				//Define the template from the above,
				//passing the package, the file name, and the map of strings to the template:
				DataObject dobj = dTemplate.createFromTemplate(df, target, args);

				//Obtain a FileObject:
				createdFiles.add(dobj.getPrimaryFile());
			}
		}

		// Return the created file.
		return createdFiles;
	}

	@Override
	public void initialize(WizardDescriptor wizard) {
		this.wizard = wizard;
		wizard.putProperty("createFolder", false);
	}

	@Override
	public void uninitialize(WizardDescriptor wizard) {
		panels = null;
	}

	@Override
	public WizardDescriptor.Panel<WizardDescriptor> current() {
		return getPanels().get(index);
	}

	@Override
	public String name() {
		return index + 1 + ". from " + getPanels().size();
	}

	@Override
	public boolean hasNext() {
		return index < getPanels().size() - 1;
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}

	@Override
	public void nextPanel() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		index++;
	}

	@Override
	public void previousPanel() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		index--;
	}

	// If nothing unusual changes in the middle of the wizard, simply:
	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}
	// If something changes dynamically (besides moving between panels), e.g.
	// the number of panels changes in response to user input, then use
	// ChangeSupport to implement add/removeChangeListener and call fireChange
	// when needed

	// You could safely ignore this method. Is is here to keep steps which were
	// there before this wizard was instantiated. It should be better handled
	// by NetBeans Wizard API itself rather than needed to be implemented by a
	// client code.
	private String[] createSteps() {
		String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
		assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
		String[] res = new String[(beforeSteps.length - 1) + panels.size()];
		for (int i = 0; i < res.length; i++) {
			if (i < (beforeSteps.length - 1)) {
				res[i] = beforeSteps[i];
			} else {
				res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
			}
		}
		return res;
	}

}
