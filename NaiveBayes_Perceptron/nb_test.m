function Pred_nb = nb_test(model, Xtest)
Pred_nb = zeros(size(Xtest,1),1);
temp = Xtest*transpose(model{1,1});
for i = 1:size(temp, 1)
if((temp(i,1) + model{1,2})> (temp(i,2)+model{1,3}))
    Pred_nb(i,:) = 1;
end

end