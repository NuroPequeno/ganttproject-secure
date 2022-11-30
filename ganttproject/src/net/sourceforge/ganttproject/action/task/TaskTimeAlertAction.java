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
import javax.swing.*;
import java.awt.*;

import java.util.GregorianCalendar;
import java.util.Comparator;
import java.util.Collections;


public class TaskTimeAlertAction extends TaskActionBase{


    final UIFacade myUIFacade;
    private GanttTree2 tree;
    public TaskTimeAlertAction(TaskManager taskManager, TaskSelectionManager selectionManager, UIFacade uiFacade, GanttTree2 tree) {
        super("task.timealert", taskManager, selectionManager, uiFacade, tree);
        myUIFacade = uiFacade;
        this.tree = tree;
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


        Comparator<Task> endDateComparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getEnd().compareTo(o2.getEnd());
            }
        };

        //Sort tasks by end date
        Collections.sort(selection, endDateComparator);

        GanttCalendar endDate;
        GregorianCalendar now = new GregorianCalendar();

        JPopupMenu menu = new JPopupMenu();
        Component c = tree.getTreeComponent();
        menu.show(c,100,100);
        int i = 0;
        JLabel label1 = new JLabel("Task in order to prescribe:");
        menu.add(label1);
        label1.setLocation(0,0);
        label1.setForeground(Color.blue);

        JSeparator separator = new JSeparator(){
            @Override
            public Dimension getMaximumSize(){
                return new Dimension(400, 5);
            }

        };
        menu.add(separator);
        separator.setLocation(0,35);
        separator.setForeground(Color.gray);

        for(Task t: selection){
            endDate =  t.getEnd();

            GregorianCalendar GregorianEnd = (GregorianCalendar)endDate;
            long diff = (GregorianEnd.getTimeInMillis() - now.getTimeInMillis())/ (1000*60*60*24)+1;

            if(t.getCompletionPercentage()<100) {
                //interessante mostrar também a percentagem do progresso
                JLabel label = new JLabel(t.getName()+"  |  "+t.getCompletionPercentage()+"  |  "+diff);
                menu.add(label);
                label.setLocation(0,40+30*i); // 30 tamanho da altura das letras + 30 descer trinta
                i++;
            }

        }
        menu.setPopupSize(200, 350);
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