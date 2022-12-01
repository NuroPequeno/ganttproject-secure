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

        List<Date> endDates = new ArrayList<>();
        GanttCalendar calendar;
        JPopupMenu menu = new JPopupMenu();
        Component c = tree.getTreeComponent();
        menu.show(c,250,50);


         Comparator<Task> endDateComparator = new Comparator<Task>() {
             @Override
             public int compare(Task o1, Task o2) {
                 return o1.getEnd().compareTo(o2.getEnd());
             }
         };
         //Sort tasks by end date
         Collections.sort(selection, endDateComparator);



        int j = 0;
        int i = 0;

        //menu.add(showLineSeparator(0,35));

        String[][] rec = new String[selection.size()][3];
        String[] header = {"name", "completion %", "days left"};


        GanttCalendar endDate;
        GregorianCalendar now = new GregorianCalendar();


        for(Task t: selection){
            j= 0;
            endDate =  t.getEnd();

            GregorianCalendar GregorianEnd = (GregorianCalendar)endDate;
            long diff = (GregorianEnd.getTimeInMillis() - now.getTimeInMillis())/ (1000*60*60*24)+1;

                 //JLabel label = new JLabel(t.getName()); // escrever na etiqueta o nome da tarefa

                 //only display not completed tasks
                 if(t.getCompletionPercentage()<100) {
                     rec[i][j++] = t.getName();
                     rec[i][j++] = String.valueOf(t.getCompletionPercentage());
                     if(diff>0) {
                         rec[i][j++] = String.valueOf(diff);
                     } else if(diff==0){
                         rec[i][j++] = "today!";
                     } else {
                         rec[i][j++] = "behind!";
                     }
                 }


            //label.setLocation(0,40+30*i); // 30 tamanho da altura das letras + 30 descer trinta
            //menu.add(panel);
            i++;

        }



        JTable table = new JTable(rec,header);
        //table.setRowSelectionAllowed(true);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setEnabled(false);
        menu.add(table);
        menu.add(new JScrollPane(table));

        menu.setPopupSize(350, 200);
    }


}