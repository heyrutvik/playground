require 'inference_rules'

def enter
  puts "\n"
end

puts "simple abstract machine"
Machine.new(
  Add.new(
    Mul.new(Number.new(1), Number.new(2)),
    Mul.new(Number.new(3), Number.new(4))
  ),
  {}
).run
enter

=begin
puts "abstract machine with variables"
Machine.new(
  Add.new(Variable.new(:x), Variable.new(:y)),
  {x: Number.new(3), y: Number.new(4)}
).run
enter
=end

puts "abstract machine with statements"
Machine.new(
  Assign.new(:x, Add.new(Variable.new(:x), Variable.new(:y))),
  {x: Number.new(2), y: Number.new(4)}
).run
enter

puts "abstract machine with if statement"
Machine.new(
  If.new(
    Variable.new(:x),
    Assign.new(:y, Number.new(1)),
    Assign.new(:y, Number.new(2))
  ),
  { x: Boolean.new(true) }
).run
enter

puts "abstract machine with sequence of operation"
Machine.new(
  Sequence.new(
    Assign.new(:x, Add.new(Number.new(1), Number.new(1))),
    Assign.new(:y, Add.new(Variable.new(:x), Number.new(3)))
  ),
  {}
).run
enter

puts "abstract machine with while loop"
Machine.new(
  While.new(
    LessThan.new(Variable.new(:x), Number.new(5)),
    Assign.new(:x, Mul.new(Variable.new(:x), Number.new(3)))
  ),
  { x: Number.new(1)}
).run
enter

puts "big-step semantics for add"
puts Add.new(Variable.new(:x), Variable.new(:y)).evaluate({x: Number.new(3), y: Number.new(4)})
enter

puts "big-step semantics for while"
while1 = While.new(
  LessThan.new(Variable.new(:x), Number.new(5)),
  Assign.new(:x, Mul.new(Variable.new(:x), Number.new(3)))
)
puts while1.evaluate({ x: Number.new(1)})
enter

puts Number.new(1).to_ruby
enter

puts "denotational semantics number"
puts eval(Number.new(3).to_ruby).call({})
enter

puts "denotational semantics assign"
puts eval(Assign.new(:y, Add.new(Variable.new(:x), Number.new(1))).to_ruby).call({ x: 3})
enter

puts ""
s1 = While.new(
  LessThan.new(Variable.new(:x), Number.new(5)),
  Assign.new(:x, Mul.new(Variable.new(:x), Number.new(3)))
)
puts s1.to_ruby
puts eval(s1.to_ruby).call({ x: 1 })
enter
