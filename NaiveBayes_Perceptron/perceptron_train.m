function model = perceptron_train(Xtrain, Ytrain)
k = 1;
t = 0;
T = 5;
w = zeros(700,size(Xtrain,2));
c = zeros(700,1);
y = Ytrain+Ytrain-1;


while(t<T)
   
    for i = 1:size(Xtrain,1)
        if(y(i,1)*dot(Xtrain(i,:),w(k,:)) <=0)
            w(k+1,:) = w(k,:) + y(i,1)*Xtrain(i,:);
            c(k+1,1) = 1;
            k = k+1;
            
        else
            c(k,1) = c(k,1) + 1;
        end
    end
    t = t+1;
end

model = {w,c,k,t};


end