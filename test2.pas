uses Sysutils;
type
  TAtomicMass = class
    private
        fString: string;
				    
	
    public
		y: Integer;
        constructor Create(S: string);
        destructor Destroy;
  end;


constructor TAtomicMass.Create(S: string;p:Integer);
begin
    fString := S;
	y:=p;
  
end;

destructor TAtomicMass.Destroy();
begin
    writeln('Object is destroying');
    
end;

var
  Atomic: TAtomicMass;

begin
  Atomic := TAtomicMass.Create('Hello',5); 
  writeln('Testing Pascal Class Features');
  t:=Atomic.y;
  writeln('Accessing public variable y: ', t);
  Atomic := TAtomicMass.Destroy();

end.
