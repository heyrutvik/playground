module Main

import Data.Vect

data DataStore : Type where
  MkData : (size : Nat) ->
           (items : Vect size String) ->
           DataStore

size : DataStore -> Nat
size (MkData size' items') = size'

items : (store : DataStore) -> Vect (size store) String
items (MkData size' items') = items'

addToStore : DataStore -> String -> DataStore
addToStore (MkData size items) item = MkData _ (items ++ [item])

data Command = Add String
             | Get Integer
             | Size
             | Search String
             | Quit

parseCommand : (cmd : String) -> (args : String) -> Maybe Command
parseCommand "add" args = Just (Add args)
parseCommand "get" num = case all isDigit (unpack num) of
                               False => Nothing
                               True => Just (Get (cast num))
parseCommand "size" "" = Just Size
parseCommand "search" query = Just (Search query)
parseCommand "quit" "" = Just Quit
parseCommand _ _ = Nothing

parse : (input : String) -> Maybe Command
parse input = case Strings.span (/= ' ') input of
                   (cmd, args) => parseCommand cmd (ltrim args)

getEntry : (pos : Integer) -> (store : DataStore) -> Maybe (String, DataStore)
getEntry pos store = let store_items = items store in
                         case integerToFin pos (size store) of
                              Nothing => Just ("Out of range\n", store)
                              Just id => Just (index id (items store) ++ "\n", store)

searchOutput : (store : DataStore) -> (x : String) -> String
searchOutput store x = case filter (\s => isInfixOf x s) (items store) of
                            (x ** pf) => unwords (toList pf)

processInput : DataStore -> String -> Maybe (String, DataStore)
processInput store input = case parse input of
                                Nothing => Just ("Invalid command\n", store)
                                (Just (Add x)) => Just ("ID " ++ show (size store) ++ "\n", addToStore store x)
                                (Just (Get x)) => getEntry x store
                                (Just Size) => Just ((cast (size store)) ++ "\n", store)
                                (Just (Search x)) => Just ((searchOutput store x) ++ "\n", store)
                                (Just Quit) => Nothing

main : IO ()
main = replWith (MkData _ []) "Command:> "processInput
