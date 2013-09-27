function perceptron_run(Xtrain, Ytrain, Xtest, Ytest)
model = perceptron_train(Xtrain, Ytrain);
Pred_per = perceptron_test(model, Xtest);
a = 0;
b = 0;
c = 0;
d = 0;
for i = 1:size(Pred_per,1)
    if(Pred_per(i,1) == 0 && Ytest(i,1) == 0)
        a = a+1;
    else
        if(Pred_per(i,1) == 1 && Ytest(i,1)==0)
            b = b+1;
        else
            if(Pred_per(i,1) == 1 && Ytest(i,1)==1)
                d = d+1;
            else
                c = c+1;
            end
        end
    end
       
end
precision = d/(b+d);
recall = d/(c+d);
accuracy = (a+d)/(a+b+c+d);
save Pred_per.mat Pred_per;
fprintf('P:%.3f, R:%.3f, A:%.3f', precision, recall, accuracy);
end