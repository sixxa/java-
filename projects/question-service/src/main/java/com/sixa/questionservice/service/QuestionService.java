package com.sixa.questionservice.service;

import com.sixa.questionservice.dao.QuestionDao;
import com.sixa.questionservice.model.Question;
import com.sixa.questionservice.model.QuestionWrapper;
import com.sixa.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
        return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("Question added", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Question not added", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        try {
            List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        try {
            List<QuestionWrapper> wrappers = new ArrayList<>();
            List<Question> questions = new ArrayList<>();

            for (Integer Id : questionIds) {
                questions.add(questionDao.findById(Id).get());
            }
            for (Question question : questions) {
                QuestionWrapper wrapper = new QuestionWrapper();
                wrapper.setId(question.getId());
                wrapper.setQuestionTitle(question.getQuestionTitle());
                wrapper.setOption1(question.getOption1());
                wrapper.setOption2(question.getOption2());
                wrapper.setOption3(question.getOption3());
                wrapper.setOption4(question.getOption4());
                wrappers.add(wrapper);
            }

            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        try {
            int right = 0;
            for (Response response : responses) {
                Question question = questionDao.findById(response.getId()).get();
                if (response.getResponse().equals(question.getRightAnswer()))
                    right++;
            }
            return new ResponseEntity<>(right, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
