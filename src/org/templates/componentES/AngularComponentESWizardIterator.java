/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.templates.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;
import org.templates.Utils;

@TemplateRegistration(
		folder = "Angular Templates",
		content = {"templates/angularComponent.js", "templates/angularComponentES.html"},
		displayName = "#AngularComponentESWizardIterator_displayName",
		description = "angularComponentES.html",
		scriptEngine = "freemarker")
@Messages("AngularComponentWizardIterator_displayName=Angular Component")
public final class AngularComponentESWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

	private int index;

	private WizardDescriptor wizard;
	private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

	private JCheckBox createFolder;

	private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
		if (createFolder == null) {
			createFolder = new JCheckBox();
			createFolder.setText("Create folder");
			createFolder.setSelected(true);
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					wizard.putProperty("createFolder", createFolder.isSelected());
				}
			};
			createFolder.addActionListener(al);
		}
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
					Component bottomPanel = jc.getComponent(jc.getComponentCount() - 1);
					if (bottomPanel instanceof JPanel) {
						JPanel bp = (JPanel) bottomPanel;
						bp.add(createFolder);
						bp.validate();
					}
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

		Set<FileObject> createdFiles = Utils.createFilesFromTemplate(wizard, "Component");

		// Return the created file.
		return createdFiles;
	}

	@Override
	public void initialize(WizardDescriptor wizard) {
		this.wizard = wizard;
		wizard.putProperty("createFolder", true);
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
