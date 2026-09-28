import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {

    // Student class
    static class Student {
        String id;
        String name;
        String course;

        Student(String id, String name, String course) {
            this.id = id;
            this.name = name;
            this.course = course;
        }
    }

    // Store students
    private ArrayList<Student> students = new ArrayList<>();

    // GUI components
    private JTextField idField;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField searchField;

    private JTable table;
    private DefaultTableModel model;

    public StudentManagementSystem() {

        setTitle("Student Management System");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Student Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        mainPanel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        formPanel.add(courseField);

        formPanel.add(new JLabel("Search by ID:"));
        searchField = new JTextField();
        formPanel.add(searchField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Table
        String[] columns = {"Student ID", "Name", "Course"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add button
        addButton.addActionListener(e -> addStudent());

        // Update button
        updateButton.addActionListener(e -> updateStudent());

        // Delete button
        deleteButton.addActionListener(e -> deleteStudent());

        // Search button
        searchButton.addActionListener(e -> searchStudent());

        // Clear button
        clearButton.addActionListener(e -> clearFields());

        // Table row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                if (row >= 0) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    courseField.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
    }

    // Add Student
    private void addStudent() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Check duplicate ID
        for (Student s : students) {
            if (s.id.equals(id)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Student ID already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        Student student = new Student(id, name, course);
        students.add(student);

        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Student added successfully!"
        );

        clearFields();
    }

    // Update Student
    private void updateStudent() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields!"
            );
            return;
        }

        for (Student s : students) {

            if (s.id.equals(id)) {

                s.name = name;
                s.course = course;

                refreshTable();

                JOptionPane.showMessageDialog(
                        this,
                        "Student updated successfully!"
                );

                clearFields();
                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student not found!"
        );
    }

    // Delete Student
    private void deleteStudent() {

        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter Student ID!"
            );
            return;
        }

        for (int i = 0; i < students.size(); i++) {

            if (students.get(i).id.equals(id)) {

                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this student?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {

                    students.remove(i);

                    refreshTable();

                    JOptionPane.showMessageDialog(
                            this,
                            "Student deleted successfully!"
                    );

                    clearFields();
                }

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student not found!"
        );
    }

    // Search Student
    private void searchStudent() {

        String searchId = searchField.getText().trim();

        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter Student ID to search!"
            );
            return;
        }

        for (Student s : students) {

            if (s.id.equals(searchId)) {

                idField.setText(s.id);
                nameField.setText(s.name);
                courseField.setText(s.course);

                JOptionPane.showMessageDialog(
                        this,
                        "Student found!"
                );

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student not found!"
        );
    }

    // Refresh table
    private void refreshTable() {

        model.setRowCount(0);

        for (Student s : students) {

            model.addRow(new Object[]{
                    s.id,
                    s.name,
                    s.course
            });
        }
    }

    // Clear fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        searchField.setText("");

        table.clearSelection();
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StudentManagementSystem app =
                    new StudentManagementSystem();

            app.setVisible(true);
        });
    }
}