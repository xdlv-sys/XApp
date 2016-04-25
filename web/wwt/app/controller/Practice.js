Ext.define('XApp.controller.Practice', {
    extend: 'Ext.app.Controller',
    questionCount: 20,
    config: {
        questions: [],
        current: 0,
        answers: [],
        refs: {
            nexButton: 'button[name=next]',
            question: 'fieldset[name=question]',

            answerA: 'answer[label=A]',
            answerB: 'answer[label=B]',
            answerC: 'answer[label=C]',
            answerD: 'answer[label=D]'
        },
        control: {
            'practice': {
                show: 'onShow'
            },
            'button[name=startTest]': {
                tap: 'startTest'
            },
            'button[name=viewReport]' :{
                tap: 'viewReport'
            },
            'button[name=next]': {
                tap: 'nextQuestion'
            },
            'answer': {
                beforeChanged: 'choseAnswer'
            }
        }
    },
    onShow: function (view) {
        //play music
        view.down('audio').play();

        Ext.Ajax.request({
            url: 'practice!obtainQuestions.cmd',
            scope: this,
            success: function (response) {
                var msg = Ext.JSON.decode(response.responseText, true);
                msg.questions.splice(this.questionCount, msg.questions.length - this.questionCount);
                this.setQuestions(msg.questions);

                this.fillCurrentQuestion();
            }
        });
        //
        this.getAnswers().push(this.getAnswerA());
        this.getAnswers().push(this.getAnswerB());
        this.getAnswers().push(this.getAnswerC());
        this.getAnswers().push(this.getAnswerD());
    },

    startTest: function (view) {
        var form = view.up('login');
        var userName = form.down('textfield[name=userName]').getValue();
        var city = form.down('selectfield').getValue();
        if (Ext.isEmpty(userName)) {
            Ext.Msg.alert("提示", "请先填写姓名");
            return;
        }
        form.destroy();
        Ext.Viewport.add(Ext.create('XApp.view.Practice', {
            userName: userName,
            city: city
        }));
    },

    viewReport : function(view){
        var form = view.up('login');
        form.destroy();
        Ext.Viewport.add(Ext.create('XApp.view.Report'));
    },

    choseAnswer: function (view, checked) {
        var currentQuestion = this.currentQuestion();
        if (currentQuestion.single) {
            this.recoveryStyle(view);
        }
    },
    recoveryStyle: function(view){
        Ext.each(this.getAnswers(), function (v, i) {
            if (view == v){
                return;
            }
            v.reset();
        });
    },
    fillCurrentQuestion: function () {
        var currentQuestion = this.currentQuestion();
        this.getQuestion().setHtml(currentQuestion.question);
        this.getAnswerA().setValue(currentQuestion.answerA);
        this.getAnswerB().setValue(currentQuestion.answerB);
        this.getAnswerC().setValue(currentQuestion.answerC);
        this.getAnswerD().setValue(currentQuestion.answerD);
    },
    showResult: function (view) {
        var right = 0;
        var fail = 0;
        Ext.each(this.getQuestions(), function (q) {
            if (q.rightAnswer == q.answer) {
                right++;
            } else {
                fail++;
            }
        });
        var practice = view.up('practice');
        var userName = practice.getInitialConfig('userName');
        var city = practice.getInitialConfig('city');
        Ext.Ajax.request({
            url: 'practice!uploadGrade.cmd',
            params: {
                'grade.city': city,
                'grade.userName': userName,
                'grade.rightCount': right,
                'grade.failCount': fail
            }
        });

        view.up('practice').destroy();
        var resultView = Ext.create('XApp.view.PracticeResult');
        Ext.Viewport.add(resultView);

        resultView.down('fieldset[name=right]').setHtml('你共答对：' + right + '题');
        resultView.down('fieldset[name=grade]').setHtml('你的得分：' + right * 5);
    },
    nextQuestion: function (view) {
        var current = this.getCurrent();
        var currentQuestion = this.currentQuestion();
        currentQuestion.answer = '';
        var choseAtLeastOne = false;

        Ext.each(this.getAnswers(), function (v) {
            if (v.isChecked()) {
                currentQuestion.answer += v.getLabel();
                choseAtLeastOne = true;
            }
        });

        if (!choseAtLeastOne) {
            new Ext.MessageBox({
                config: {
                    ui: 'light'
                }
            }).alert('提示', '至少选择一个答案。');
            return;
        }
        var tip = '恭喜你，答对';
        if (currentQuestion.rightAnswer != currentQuestion.answer) {
            tip = '继续努力，正确答案为： ' + currentQuestion.rightAnswer;
        }
        Ext.Msg.alert('结果', tip, function () {
            this.setCurrent(current + 1);
            if (this.getCurrent() == this.questionCount) {
                this.showResult(view);
                return;
            }
            this.recoveryStyle();
            this.fillCurrentQuestion();
            view.setText('下一题&nbsp;&nbsp;(' + (this.getCurrent() + 1) + '/' + this.questionCount + ')');
        }, this);
    },
    currentQuestion: function () {
        var current = this.getCurrent();
        return this.getQuestions()[current];
    }
});
