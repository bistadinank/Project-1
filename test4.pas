uses Sysutils;
type
  TAtomicMass = class
    private
        fString: string;
		y: Integer;		    	
    public		
        function GetString: string;
        constructor Create(S: string);
        destructor Destroy;
  end;

function TAtomicMass.GetString(): string;
begin
	
    result := fString;
	writeln('calling from function ', result);
end;

constructor TAtomicMass.Create(S: string;p:Integer);
begin
    fString := S;
	y:=p;
    writeln('Object is created with string value: ', fString);
	writeln('Object is created with Integer value: ', y);
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
  x:= 15;
  t:=Atomic.GetString();
  writeln('Value of t: ', t);
  Atomic := TAtomicMass.Destroy();

end.
