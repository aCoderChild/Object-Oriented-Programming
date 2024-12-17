import java.util.ArrayList;
import java.util.List;

public class StudentManagement {
    Student[] students = new Student[100]; //MAX_STUDENTS = 100
    int numOfStudents = 0;

    /**
     *
     * @param s1 sinh vien 1
     * @param s2 sinh vien 2
     * @return true or false
     */
    public static boolean sameGroup(Student s1, Student s2) {
        return s1.getGroup().equals(s2.getGroup());
    }

    public void addStudent(Student newStudent) {
        students[numOfStudents] = newStudent;
        numOfStudents++;
    }

    public String studentsByGroup() {
        List<String> listOfGroups = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < numOfStudents; i++) {
            if (listOfGroups.contains(students[i].getGroup())) {
                continue;
            }
            listOfGroups.add(students[i].getGroup());
        }
        for (String group : listOfGroups) {
            res.append(group).append("\n");
            for (int i = 0; i < numOfStudents; i++) {
                if (students[i].getGroup().equals(group)) {
                    res.append(students[i].getInfo()).append("\n");
                }
            }
        }
        return res.toString();
    }

    public void removeStudent(String id) {
        for (int i = 0; i < numOfStudents; i++) {
            if (students[i].getId().equals(id)) {
                for (int j = i; j < numOfStudents - 1; j++) {
                    students[j] = students[j + 1];
                }
                numOfStudents--;
                break;
            }
        }
    }

    public static void main(String[] args) {
        StudentManagement studentManagement = new StudentManagement();
        Student s1 = new Student("Nguyen Van An", "17020001", "@vnu.edu.vn");
        Student s2 = new Student("Nguyen Van B", "17020002", "@vnu.edu.vn");
        Student s3 = new Student("Nguyen Van C", "17020003","@vnu.edu.vn");
        s3.setGroup("K62CB");
        Student s4 = new Student("Nguyen Van D", "17020004","@vnu.edu.vn");
        s4.setGroup("K62CB");
        String ans = studentManagement.studentsByGroup();
        System.out.println(ans);
    }
}
