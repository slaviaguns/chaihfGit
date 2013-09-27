function Pred_per = perceptron_test(model, Xtest)

Pred_per = zeros(size(Xtest,1),1);
%a = sign(Xtest * transpose(model{1,1}));
Pred_per = sign(sign(Xtest * transpose(model{1,1}))*model{1,2});
temp = Pred_per+1;
Pred_per = (temp == 2);

%a(temp > 0);

end