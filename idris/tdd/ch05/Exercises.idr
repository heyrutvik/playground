module Exercises

import System
import Data.Vect

greeting : IO ()
greeting = do
  putStr "Enter your name: "
  x <- getLine
  putStrLn ("Hello " ++ x ++ "!")

printLength : IO ()
printLength = putStr "Input string: " >>= \_ =>
              getLine >>= \input =>
              let len = length input in
              putStrLn (show len)

printLonger : IO ()
printLonger = do
  first <- getLine
  let fl = length first
  second <- getLine
  let sl = length second
  let max = if fl > sl then fl else sl
  putStrLn ("longer string has " ++ (cast max) ++ " characters..")

printLonger' : IO ()
printLonger' = do
  getLine >>= \first =>
  let fl = length first in
  getLine >>= \second =>
  let sl = length second in
  let max = if fl > sl then fl else sl in
  putStrLn ("longer string has " ++ (cast max) ++ " characters..")

getNumber : IO (Maybe Integer)
getNumber = do
  putStr ("Please your guess: ")
  input <- getLine
  case all isDigit (unpack input) of
                 False => pure Nothing
                 True => pure (Just (cast input))

guess' : (target : Integer) -> IO ()
guess' target = do
  Just num <- getNumber | Nothing => guess' target
  case compare num target of
       LT => putStrLn ("Your guess is too law..") >>= \_ => (guess' target)
       EQ => putStrLn ("Congrats.. correct guess!!")
       GT => putStrLn ("Your guess is too high..") >>= \_ => (guess' target)

guess'' : (target : Integer) -> (guesses : Nat) -> IO ()
guess'' target guesses = do
  Just num <- getNumber | Nothing => guess'' target (guesses + 1)
  case compare num target of
       LT => putStrLn ("Your guess is too law.. (attempt: " ++ (cast guesses) ++ ")") >>= \_ => (guess'' target (guesses + 1))
       EQ => putStrLn ("Congrats.. correct guess!! (attempt: " ++ (cast guesses) ++ ")")
       GT => putStrLn ("Your guess is too high..(attempt: " ++ (cast guesses) ++ ")") >>= \_ => (guess'' target (guesses + 1))

guess : IO ()
guess = time >>= \t => guess'' (t `mod` 100) 1

my_repl : (prompt : String) -> (onInput : String -> String) -> IO ()
my_repl prompt onInput = do
  putStr prompt
  input <- getLine
  putStr (onInput input)
  my_repl prompt onInput

my_replWith : (state : a) -> (prompt : String) -> (onInput : a -> String -> Maybe (String, a)) -> IO ()
my_replWith state prompt onInput = do
  putStr prompt
  input <- getLine
  case onInput state input of
       Nothing => pure ()
       (Just (output, newState)) => do
         putStr output
         my_replWith newState prompt onInput

my_replWith_test : IO ()
my_replWith_test = my_replWith [] "write: " processInput where
  processInput : List String -> String -> Maybe (String, List String)
  processInput xs "" = Nothing
  processInput xs input = (Just ("total input: " ++ (cast (length xs)) ++ "\n", input :: xs))

readToBlank : IO (List String)
readToBlank = do
  x <- getLine
  if (x == "")
    then pure []
    else do xs <- readToBlank
            pure (x :: xs)

readAndSave : IO ()
readAndSave = do
  putStrLn "enter some text: "
  text <- readToBlank
  putStr "enter file path to store text: "
  filePath <- getLine
  Right _ <- writeFile filePath (unlines text) | Left err => pure ()
  putStrLn "text stored."

readVect : (file : File) -> IO (n ** Vect n String)
readVect file = do
  eof <- fEOF file
  if (eof == True)
    then pure (_ ** [])
    else (
      do
        Right line <- fGetLine file | Left err => pure (_ ** [])
        (_ ** lines) <- readVect file
        pure (_ ** line :: lines)
    )

readVectFile : (filename : String) -> IO (n ** Vect n String)
readVectFile filename = do
  Right file <- openFile filename Read | Left err => pure (_ ** [])
  content <- readVect file
  closeFile file
  pure content
