package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gui {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTable IssueTable;
    private JButton addButton;
    private JTextField textFieldDescription;
    private JComboBox comboBoxPriority;
    private JComboBox comboBoxEmployee;
    private JSpinner spinner1;
    private JLabel dateLabel;
    private JLabel statusLabel;
    private JLabel PriorityLabel;
    private JLabel DescriptionLabel;
    private JLabel employeeLabel;
    private JComboBox comboBoxStatus;
    private JButton refreshButton;

    public Gui() throws SQLException {
        String userName = "root";
        String password = "pass";
        String connectionURl = "jdbc:mysql://localhost:3306/clientServer";

        List<Issues> issuesList = new ArrayList<Issues>();
        List<Employee> employeeList = new ArrayList<Employee>();

        try (Connection conn = DriverManager.getConnection(connectionURl, userName, password);
             Statement statement = conn.createStatement()) {
            System.out.println("Подключение успешно");

//            statement.executeUpdate("drop table issues");
//            statement.executeUpdate("drop table employee");
//            statement.executeUpdate("create table employee (id int not null auto_increment, name char(30), surname char(20), job char(40), primary key (id));");
//            statement.executeUpdate("create table issues (id int not null auto_increment, description char(30), priority char(20), data char(40), status char(20), primary key (id), employee_id int, foreign key (employee_id) references employee (id));");
//
//            //statement.executeUpdate("insert into issues set description = 'Задача 1', priority = 'Важно',data = '23.10.2022',status = 'В работе'");
//            statement.executeUpdate("insert into employee set name = 'Савелий', surname = 'Останин',job = 'Программист'");
//            statement.executeUpdate("insert into employee set name = 'Максим', surname = 'Косяков',job = 'Хороший Мальчик'");

            System.out.print("id Не выполненных задач: ");
            ResultSet notDone = statement.executeQuery("{call getNotDone}");
            while (notDone.next()) {
                System.out.print(notDone.getString(1) + ", ");
            }

            String s = "";
            String s1 = "";

            System.out.println();
            System.out.print("Процент заявок 'нет сети' или 'не включается:'");
            ResultSet noNet = statement.executeQuery("{call noNet}");
            while (noNet.next()) {
                s = noNet.getString(1);
            }
            ResultSet countIssues = statement.executeQuery("{call countIssues}");
            while (countIssues.next()) {
                s1 =  countIssues.getString(1);
            }
            if (Integer.parseInt(s) == 0){
                System.out.println("Таких заявок нет");
            }else {
                System.out.println(100 / Integer.parseInt(s1) * Integer.parseInt(s) + "%");
            }

            ResultSet resultIssue = statement.executeQuery("select * from issues");
            while (resultIssue.next()) {
                Issues issue = new Issues();
                issue.setId(Integer.parseInt(resultIssue.getString(1)));
                issue.setDescription(resultIssue.getString(2));
                issue.setPriority(resultIssue.getString(3));
                issue.setStatus(resultIssue.getString(5));
                issue.setEmpoyeeId(Integer.parseInt(resultIssue.getString(6)));
                issuesList.add(issue);
            }

            ResultSet resultEmployee = statement.executeQuery("select * from employee");
            while (resultEmployee.next()) {
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(resultEmployee.getString(1)));
                employee.setName(resultEmployee.getString(2));
                employee.setSurname(resultEmployee.getString(3));
                employeeList.add(employee);
            }


        }



            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("id");
            tableModel.addColumn("Описание");
            tableModel.addColumn("Приоритет");
            tableModel.addColumn("Статус");
            tableModel.addColumn("Срок выполнения");
            tableModel.addColumn("Id Исполнителя");
            IssueTable.setModel(tableModel);
            for (Issues issues : issuesList) {
                tableModel.addRow(new Object[]{issues.getId(), issues.getDescription(), issues.getPriority(), issues.getStatus(), issues.getDate(), issues.getEmpoyeeId()});
            }

            SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
            spinner1.setModel(spinnerDateModel);

            comboBoxEmployee.setEditable(true);
            comboBoxPriority.setEditable(true);

            for (Employee employee : employeeList) {
                comboBoxEmployee.addItem(employee.getName() + " " + employee.getSurname());
            }

            String[] priorityItems = {
                    "Категорически важно",
                    "Крайне важно",
                    "Важно",
                    "Маловажно",
                    "Не важно"
            };

            String[] StatusItems = {
                    "Выполнено",
                    "В работе"
            };

            for (String s : StatusItems) {
                comboBoxStatus.addItem(s);
            }

            for (String s : priorityItems) {
                comboBoxPriority.addItem(s);
            }


            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Issues issue = new Issues();
                    issue.setDescription(textFieldDescription.getText());
                    issue.setPriority((String) comboBoxPriority.getSelectedItem());
                    issue.setDate((Date) spinner1.getValue());
                    issue.setStatus((String) comboBoxStatus.getSelectedItem());

                    String fullName = (String) comboBoxEmployee.getSelectedItem();
                    try (Connection conn = DriverManager.getConnection(connectionURl, userName, password);
                         Statement statement = conn.createStatement();) {
                        ResultSet resultEmployeeId = statement.executeQuery("select id from employee where name = '" +
                                getName(fullName) + "' AND surname = '" +
                                getSurname(fullName) + "'");
                        while (resultEmployeeId.next()) {
                            issue.setEmpoyeeId(Integer.parseInt(resultEmployeeId.getString(1)));
                        }
                        statement.executeUpdate("insert into issues set description = '" + issue.getDescription() +
                                "', priority = '" + issue.getPriority() + "' , data = '" + issue.getDate() +
                                "', employee_id = '" + issue.getEmpoyeeId() + "', status = '" + issue.getStatus() + "'");
                        tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, issue.getDescription(), issue.getPriority(), issue.getStatus(), issue.getDate(), issue.getEmpoyeeId()});

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                }
            });

        }

    public String getName(String fullName){
        return fullName.split(" (?!.* )")[0];
    }

    public String getSurname(String fullName){
        return fullName.split(" (?!.* )")[1];
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JFrame frame = new JFrame("GUIMain.java");
        frame.setContentPane(new Gui().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 400);
    }
}
