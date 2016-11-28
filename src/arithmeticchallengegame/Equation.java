package arithmeticchallengegame;

/**
 *
 * @author Daniel Field
 */
public class Equation {
    // <editor-fold desc="vars">
    String dateTime;
    
    float operandOne;
    
    char operator;
    
    float operandTwo;
    
    float answerGiven;
    
    float correctAnswer;
    
    
    // </editor-fold>
    
    // <editor-fold desc="constructor(s)">
    public Equation(float operandOne, char operator, float operandTwo, float answer) {
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
        this.operator = operator;
        this.answerGiven = answer;
        
        dateTime = Utilities.getCurrentDateTimeAsString("yyyy/MM/dd HH:mm:ss");
        
        correctAnswer = Utilities.calculateAnswer(operandOne, operator+"", operandTwo);
    }
    
    public Equation(String dateTime, float operandOne, char operator, float operandTwo, float answer) {
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
        this.operator = operator;
        this.answerGiven = answer;
        this.dateTime = dateTime;
        
        correctAnswer = Utilities.calculateAnswer(operandOne, operator+"", operandTwo);
    }
    // </editor-fold>
    
    // true if answer is correct, else false.
    public boolean isCorrect() {
        return Utilities.calculateAnswer(operandOne, operator+"", operandTwo) == answerGiven;
    }
    
    // Converts the Equation into a string array and returns it.
    public String[] toStringArray() {
        // Strip .0 when it is not needed.
        String op1 = Utilities.stripZero(operandOne+"");
        String op2 = Utilities.stripZero(operandTwo+"");
        String ans = Utilities.stripZero(answerGiven+"");
        
        return new String[]{ op1, operator+"", op2, ans };
    }
    
    // <editor-fold desc="getters and setters">
    
    // <editor-fold desc="date and time methods">
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    public String getDayOfMonth() {
        return dateTime.split("/")[2].substring(0, 2);
    }
    // </editor-fold>

    public float getOperandOne() {
        return operandOne;
    }

    public void setOperandOne(int operandOne) {
        this.operandOne = operandOne;
    }

    public float getOperandTwo() {
        return operandTwo;
    }

    public void setOperandTwo(int operandTwo) {
        this.operandTwo = operandTwo;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public char getOperator() {
        return operator;
    }

    public float getAnswer() {
        return answerGiven;
    }

    public void setAnswer(float answer) {
        this.answerGiven = answer;
    }

    public float getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(float correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    // </editor-fold>
}
