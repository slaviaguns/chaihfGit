function model= nb_train(Xtrain, Ytrain)
%OneFileNum = sum(Ytrain, 1);
%AllFileNum = size(Ytrain,1);
OneFileNum = sum(Ytrain, 1);
AllFileNum = size(Ytrain,1);
p1 = log(OneFileNum/AllFileNum);
p2 = log((AllFileNum - OneFileNum)/AllFileNum);

temp = transpose(Ytrain);
WordCountInAllOneFile = temp*Xtrain;
temp1 = transpose(Ytrain == 0);
WordCountInAllZeroFine = temp1*Xtrain;
% 
AllWordInOneFile = sum(WordCountInAllOneFile, 2)+size(Xtrain, 2); %laplaced
WordCountInAllOneFile = WordCountInAllOneFile + 1; %laplace
% 
 AllWordInZeroFile = sum(WordCountInAllZeroFine, 2) + size(Xtrain, 2);
 WordCountInAllZeroFine = WordCountInAllZeroFine + 1;
p(1,:) = log(WordCountInAllOneFile./AllWordInOneFile);
p(2,:) = log(WordCountInAllZeroFine./AllWordInZeroFile);
model={p, p1, p2};

%printf('%d + %d = %d', 1, 1, 1 + 1);
       
end