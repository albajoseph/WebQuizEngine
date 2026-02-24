package engine;

import java.util.ArrayList;
import java.util.List;

public class Answer {
    private List<Integer> answer = new ArrayList<>();

    public Answer() {}

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer == null ? new ArrayList<>() : answer;
    }
}