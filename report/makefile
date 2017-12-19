./contents/img/Class_Diagram_-_Transaction.png:
	dia --export=./contents/img/Class_Diagram_-_Transaction.png --filter=png ../requirements/dia/Class_Diagram_-_Transaction.dia

./contents/img/Package_Diagram.png: ../requirements/dia/Package_Diagram.dia
	dia --export=./contents/img/Package_Diagram.png --filter=png ../requirements/dia/Package_Diagram.dia

./contents/img/Activity_Diagram_-_Categorise_Entries.png: ../requirements/dia/Activity_Diagram_-_Categorise_Entries.dia
	dia --export=./contents/img/Activity_Diagram_-_Categorise_Entries.png --filter=png ../requirements/dia/Activity_Diagram_-_Categorise_Entries.dia

./contents/img/Class_Diagram_-_Categories_and_Amount.png: ../requirements/dia/Class_Diagram_-_Categories_and_Amount.dia
	dia --export=./contents/img/Class_Diagram_-_Categories_and_Amount.png --filter=png ../requirements/dia/Class_Diagram_-_Categories_and_Amount.dia


./contents/img/Activity_Diagram_-_Manual_Entry.png: ../requirements/dia/Activity_Diagram_-_Manual_Entry.dia 
	dia --export=./contents/img/Activity_Diagram_-_Manual_Entry.png --filter=png ../requirements/dia/Activity_Diagram_-_Manual_Entry.dia 

./contents/img/Use_Case_Diagram.png: ../requirements/dia/Use_Case_Diagram.dia 
	dia --export=./contents/img/Use_Case_Diagram.png --filter=png ../requirements/dia/Use_Case_Diagram.dia 

pdf:	report.tex ./contents/img/Activity_Diagram_-_Categorise_Entries.png ./contents/img/Activity_Diagram_-_Manual_Entry.png ./contents/img/Use_Case_Diagram.png ./contents/img/Class_Diagram_-_Categories_and_Amount.png ./contents/img/Package_Diagram.png ./contents/img/Class_Diagram_-_Transaction.png
	pdflatex report && biber report && pdflatex report && pdflatex report
