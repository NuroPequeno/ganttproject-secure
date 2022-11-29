package net.sourceforge.ganttproject.action.task;

import biz.ganttproject.core.time.GanttCalendar;
import net.sourceforge.ganttproject.GanttTree2;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager;
import net.sourceforge.ganttproject.gui.UIUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskTimeAlertAction extends TaskActionBase{
    public TaskTimeAlertAction(TaskManager taskManager, TaskSelectionManager selectionManager, UIFacade uiFacade, GanttTree2 tree) {
        super("task.timealert", taskManager, selectionManager, uiFacade, tree);
    }
    @Override
    protected String getIconFilePrefix() {
        return "timealert_";
    }

    @Override
    protected boolean isEnabled(List<Task> selection) {
        return !selection.isEmpty();
    }

    @Override
    protected void run(List<Task> selection) throws Exception {
        //TODO Get all selected tasks and check end date
        // compare end date to todays date
        List<Date> endDates = new ArrayList<>();
        GanttCalendar calendar;
        for(Task t: selection){
            calendar =  t.getEnd();
            System.out.print(calendar.toString());

        }
        //getSelectionManager().fireSelectionChanged();

    }

    @Override
    public TaskTimeAlertAction asToolbarAction(){
        final TaskTimeAlertAction result = new TaskTimeAlertAction(getTaskManager(), getSelectionManager(), getUIFacade(), getTree());
        result.setFontAwesomeLabel(UIUtil.getFontawesomeLabel(result));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    result.setEnabled((Boolean)evt.getNewValue());
                }
            }
        });
        result.setEnabled(this.isEnabled());
        return result;
    }
}
