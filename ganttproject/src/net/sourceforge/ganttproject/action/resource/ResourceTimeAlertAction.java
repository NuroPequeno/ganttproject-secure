package net.sourceforge.ganttproject.action.resource;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.resource.HumanResourceManager;

import java.awt.event.ActionEvent;

public class ResourceTimeAlertAction extends ResourceAction{

    private UIFacade myUIFacade;
    private GanttProject myProject;

    public ResourceTimeAlertAction(String name, HumanResourceManager hrManager) {
        super(name, hrManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
