# number expression
class Number < Struct.new(:value)
  def to_s
    value.to_s
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    false
  end
  def evaluate(environment)
    self
  end
  def to_ruby
    "-> e { #{value.inspect} }"
  end
end

# boolean value
class Boolean < Struct.new(:value)
  def to_s
    value.to_s
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    false
  end
  def evaluate(environment)
    self
  end
  def to_ruby
    "-> e { #{value.inspect} }"
  end
end

# variable expression
class Variable < Struct.new(:name)
  def to_s
    name.to_s
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(evnironment)
    evnironment[name]
  end
  def evaluate(environment)
    environment[name]
  end
  def to_ruby
    "-> e { e[#{name.inspect}] }"
  end
end

# add expression
class Add < Struct.new(:left, :right)
  def to_s
    "#{left} + #{right}"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(evnironment)
    if left.reducible?
      Add.new(left.reduce(evnironment), right)
    elsif right.reducible?
      Add.new(left, right.reduce(evnironment))
    else
      Number.new(left.value + right.value)
    end
  end
  def evaluate(environment)
    Number.new(left.evaluate(environment).value + right.evaluate(environment).value)
  end
  def to_ruby
    "-> e { (#{left.to_ruby}).call(e) + (#{right.to_ruby}).call(e) }"
  end
end

# multiplication expression
class Mul < Struct.new(:left, :right)
  def to_s
    "#{left} * #{right}"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(evnironment)
    if left.reducible?
      Mul.new(left.reduce(evnironment), right)
    elsif right.reducible?
      Mul.new(left, right.reduce(evnironment))
    else
      Number.new(left.value * right.value)
    end
  end
  def evaluate(environment)
    Number.new(left.evaluate(environment).value * right.evaluate(environment).value)
  end
  def to_ruby
    "-> e { (#{left.to_ruby}).call(e) * (#{right.to_ruby}).call(e) }"
  end
end

# lessThan expression
class LessThan < Struct.new(:left, :right)
  def to_s
    "#{left} < #{right}"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(environment)
    if left.reducible?
      LessThan.new(left.reduce(environment), right)
    elsif right.reducible?
      LessThan.new(left, right.reduce(environment))
    else
      Boolean.new(left.value < right.value)
    end
  end
  def evaluate(environment)
    Boolean.new(left.evaluate(environment).value < right.evaluate(environment).value)
  end
  def to_ruby
    "-> e { (#{left.to_ruby}).call(e) < (#{right.to_ruby}).call(e) }"
  end
end

# do nothing statement
class DoNothing
  def to_s
    'do-nothing'
  end
  def inspect
    "<<#{self}>>"
  end
  def ==(other_statement)
    other_statement.instance_of?(DoNothing)
  end
  def reducible?
    false
  end
  def evaluate(environment)
    environment
  end
end

# assignment statement
class Assign < Struct.new(:name, :expression)
  def to_s
    "#{name} = #{expression}"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(environment)
    if expression.reducible?
      [Assign.new(name, expression.reduce(environment)), environment]
    else
      [DoNothing.new, environment.merge({ name => expression })]
    end
  end
  def evaluate(environment)
    environment.merge({name => expression.evaluate(environment)})
  end
end

# if statement
class If < Struct.new(:condition, :consequence, :alternative)
  def to_s
    "if (#{condition}) { #{consequence} } else { #{alternative} }"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(environment)
    if condition.reducible?
      [If.new(condition.reduce(environment), consequence, alternative), environment]
    else
      case condition
      when Boolean.new(true)
        [consequence, environment]
      when Boolean.new(false)
        [alternative, environment]
      end
    end
  end
  def evaluate(environment)
    case condition.evaluate(environment)
    when Boolean.new(true)
      consequence.evaluate(environment)
    when Boolean.new(false)
      alternative.evaluate(environment)
    end
  end
end

# create sequence of statements
class Sequence < Struct.new(:first, :second)
  def to_s
    "#{first}; #{second}"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(environment)
    case first
    when DoNothing.new
      [second, environment]
    else
      reduced_first, reduced_environment = first.reduce(environment)
      [Sequence.new(reduced_first, second), reduced_environment]
    end
  end
  def evaluate(environment)
    second.evaluate(first.evaluate(environment))
  end
end

# while loop using if and sequence
class While < Struct.new(:condition, :body)
  def to_s
    "while (#{condition}) { #{body} }"
  end
  def inspect
    "<<#{self}>>"
  end
  def reducible?
    true
  end
  def reduce(environment)
    [If.new(condition, Sequence.new(body, self), DoNothing.new), environment]
  end
  def evaluate(environment)
    case condition.evaluate(environment)
    when Boolean.new(true)
      evaluate(body.evaluate(environment))
    when Boolean.new(false)
      environment
    end
  end
end

# abstract machine
class Machine < Struct.new(:statement, :evnironment)
  def step
    self.statement, self.evnironment = statement.reduce(evnironment)
  end
  def run
    while statement.reducible?
      puts "#{statement}, #{evnironment}"
      step
    end
    puts "#{statement}, #{evnironment}"
  end
end
