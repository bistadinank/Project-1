uses Sysutils;
type
  TAtomicMass = class
    private
        fString: string;				    
	protected
	    x:Integer;
    public
		y: Integer;
        constructor Create(S: string);
        destructor Destroy;
  end;
constructor TAtomicMass.Create(S: string;p:Integer;a:Integer);
begin
    fString := S;
	y:=p;
	x:=a;
  
end;

destructor TAtomicMass.Destroy();
begin
    writeln('Object is destroying');
    
end;

var
  Atomic: TAtomicMass;

begin
  Atomic := TAtomicMass.Create('Hello',5,10); 
  writeln('Testing Pascal Class Features');
  t:=Atomic.y;
  writeln('Accessing public variable y: ', t);
  s:=Atomic.fString;
  i:=Atomic.x;
  Atomic := TAtomicMass.Destroy();

end.
