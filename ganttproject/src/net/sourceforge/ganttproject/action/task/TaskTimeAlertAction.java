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
import java.util.Calendar;
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

    private JSeparator showLineSeparator(int x, int y){
        JSeparator separator = new JSeparator(){
            @Override
            public Dimension getMaximumSize(){
                return new Dimension(400, 5);
            }

        };

        separator.setLocation(x,y);
        separator.setForeground(Color.gray);
        return separator;
    }




     @Override
    protected void run(List<Task> selection) throws Exception {


        JPopupMenu menu = new JPopupMenu();
        Component c = tree.getTreeComponent();
        menu.show(c,c.getBounds().width,0);


         Comparator<Task> endDateComparator = new Comparator<Task>() {
             @Override
             public int compare(Task o1, Task o2) {
                 return o1.getEnd().compareTo(o2.getEnd());
             }
         };
         //Sort tasks by end date
         Collections.sort(selection, endDateComparator);


         int i = 0;

        String[][] rec = new String[selection.size()][3];
        String[] header = {getI18n("tm.name"), getI18n("tm.completed"), getI18n("tm.days.left")};

        GanttCalendar endDate;
        GregorianCalendar now = new GregorianCalendar();
         now.set(Calendar.HOUR_OF_DAY, 0);
         now.set(Calendar.MINUTE, 0);
         now.set(Calendar.SECOND, 0);
         now.set(Calendar.MILLISECOND, 0);

        for(Task t: selection){
            endDate =  t.getEnd();

            endDate.set(Calendar.HOUR_OF_DAY, 0);
            endDate.set(Calendar.MINUTE, 0);
            endDate.set(Calendar.SECOND, 0);
            endDate.set(Calendar.MILLISECOND, 0);


            long diff = ((endDate.getTimeInMillis() - now.getTimeInMillis())/ (1000*60*60*24))-1;

                 if(t.getCompletionPercentage()<100) { //only display not completed tasks

                     rec[i][0] = t.getName();
                     rec[i][1] = String.valueOf(t.getCompletionPercentage())+" %";

                     if(diff>0) {
                         rec[i][2] = String.valueOf(diff)+ " days left";
                     } else if(diff==0){
                         rec[i][2] = "due today!";
                     } else {
                         rec[i][2] = String.valueOf(-diff) + " days behind!";
                     }
                     i++;
                     //tava com um problema no days left, já tratei. Aproveitei e mudei e alterei para esta forma entende-se melhor
                 }


        }

        JTable table = new JTable(rec,header);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setEnabled(false);
        menu.add(table);
        menu.add(new JScrollPane(table));
        menu.setPopupSize(350, 200);
    }


}