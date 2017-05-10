# BlindfoldChessTrainer

The blindfold chess trainer is an advanced chess training program that allows the user to play blindfold against multiple chess engines at a time and without overloading the computer.

The chess board representation, board evaluations and move executions were done following a series of youtube tutorials by Amir Afghani at https://www.youtube.com/watch?v=h8fSdSUKttk&list=PLOJzCFLZdG4zk5d-1_ah2B4kqZSeIlWtt.

This interface, apart from including my own basic chess engine, has the flexibility to include any other executable chess engines such as StockFish and Komodo, both free source and included in the Engines folder, which are two of the top current chess engines.

Connecting UCI engines to the GUI was partly done thanks to code from https://github.com/tondeur-h/UCIChess/blob/master/src/ucichess/UCIChess.java.

The program is completely functional, some testing is still necessary though. Before release I will remove the default custom engine and I will replace it, whether with a free source engine, such as the two mentioned above, or write one myself in C++ and make it UCI compliant to be run on any other chess GUI such as mine.
