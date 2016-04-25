package xd.fw.action;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.bean.Grade;
import xd.fw.bean.mapper.GradeMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PracticeAction extends BaseAction{
    static ArrayList<Question> questionList = new ArrayList<>();

    List<Question> questions;

    Grade grade;
    @Autowired
    GradeMapper gradeMapper;

    List<Grade> grades;

    public synchronized String obtainQuestions(){
        Collections.shuffle(questionList);
        questions = questionList;
        return SUCCESS;
    }

    public String uploadGrade()throws Exception{
        gradeMapper.insert(grade);
        return FINISH;
    }

    public String obtainReports() throws Exception{
        grades = gradeMapper.selectReports();
        int sum = 0;
        for (Grade grade : grades){
            sum += grade.getRightCount();
        }
        for (Grade grade : grades){
            grade.setFailCount(grade.getRightCount() * 100 /sum);
        }
        return SUCCESS;
    }

    static {
        Scanner scanner = new Scanner(PracticeAction.class.getResourceAsStream(
                "/signal.txt"),"UTF-8");
        String line, answer;
        String[] answers;
        String regex = "（\\s*([A-D]*)\\s*）";
        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher;
        //boolean find;
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            if (StringUtils.isBlank(line)){
                continue;
            }
            Question question = new Question();

            matcher = pattern2.matcher(line);
            matcher.find();
            question.setRightAnswer(matcher.group(1));
            question.setQuestion(line.replaceAll(regex, "____"));

            question.setAnswerA(scanner.nextLine());
            question.setAnswerB(scanner.nextLine());
            question.setAnswerC(scanner.nextLine());
            question.setAnswerD(scanner.nextLine());

            questionList.add(question);
        }
        scanner.close();

        scanner = new Scanner(PracticeAction.class.getResourceAsStream(
                "/multi.txt"),"UTF-8");
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            if (StringUtils.isBlank(line)){
                continue;
            }
            Question question = new Question();
            question.setSingle(false);
            matcher = pattern2.matcher(line);
            matcher.find();
            question.setRightAnswer(matcher.group(1));
            question.setQuestion(line.replaceAll(regex, "____"));

            question.setAnswerA(scanner.nextLine());
            question.setAnswerB(scanner.nextLine());
            question.setAnswerC(scanner.nextLine());
            question.setAnswerD(scanner.nextLine());

            questionList.add(question);
        }
        scanner.close();
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public static class Question{
        String question;
        String answerA,answerB,answerC,answerD;
        String rightAnswer;
        boolean single = true;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswerA() {
            return answerA;
        }

        public void setAnswerA(String answerA) {
            this.answerA = answerA;
        }

        public String getAnswerB() {
            return answerB;
        }

        public void setAnswerB(String answerB) {
            this.answerB = answerB;
        }

        public String getAnswerC() {
            return answerC;
        }

        public void setAnswerC(String answerC) {
            this.answerC = answerC;
        }

        public String getAnswerD() {
            return answerD;
        }

        public void setAnswerD(String answerD) {
            this.answerD = answerD;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public boolean isSingle() {
            return single;
        }

        public void setSingle(boolean single) {
            this.single = single;
        }
    }

}