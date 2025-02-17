uses Sysutils;
type
  TAtomicMass = class
    private
        fString: string;
			    	
    public
		
        constructor Create(S: string);
        destructor Destroy;
  end;


constructor TAtomicMass.Create(S: string);
begin
    fString := S;
    writeln(fString);
end;

destructor TAtomicMass.Destroy();
begin
    writeln('Object is destroying');
    
end;

var
  Atomic: TAtomicMass;

begin
  Atomic := TAtomicMass.Create('I am creating an object'); 

  Atomic := TAtomicMass.Destroy();

end.
