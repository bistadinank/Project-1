uses Sysutils;
type
  TAtomicMass = class
    private
        fString: string;
		y: Integer;		    
	
    public
        constructor Create(S: string);
        destructor Destroy;
  end;

constructor TAtomicMass.Create(S: string);
begin
    fString := S;
	y:= 5;
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
  Atomic := TAtomicMass.Create('Hello'); 
  writeln('Testing Pascal Class Features');
  Atomic := TAtomicMass.Destroy();

end.
