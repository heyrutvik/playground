module FCTypes

StringOrInt : Bool -> Type
StringOrInt x = case x of
                     True => Int
                     False => String

getStringOrInt : (x : Bool) -> StringOrInt x
getStringOrInt True = 94
getStringOrInt False = "Ninety four"
