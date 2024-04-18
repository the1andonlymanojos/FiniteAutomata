//prolog program to solve water jug problem

%initial state
initial(0,0).

%goal state
goal(2,_).

%actions
action(0,1).
action(1,0).
action(1,2).
action(2,0).
action(2,1).
action(0,2).

%state transition
next((X,Y),(Z,4)):- action(X,Z), Y=4.
next((X,Y),(4,Z)):- action(Y,Z), X=4.
next((X,Y),(Z,0)):- action(X,Z), Y=0.
next((X,Y),(0,Z)):- action(Y,Z), X=0.
next((X,Y),(Z,W)):- action(X,Z), action(Y,W), Z+W =< 4, Z+W >= 0.

%path

path(Start,Goal,Path):- path(Start,Goal,[Start],Path).
path(Goal,Goal,Path,Path).
path(Start,Goal,Visited,Path):- next(Start,Next), not(member(Next,Visited)), path(Next,Goal,[Next|Visited],Path).

%print path
printpath([]).
printpath([H|T]):- print(H), nl, printpath(T).

%find solution
solve:- initial(Start), goal(Goal), path(Start,Goal,Path), printpath(Path).