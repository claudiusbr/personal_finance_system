./contents/img/Activity_Diagram_-_Categorise_Entries.png:
	dia --export=./contents/img/Activity_Diagram_-_Categorise_Entries.png --filter=png ../requirements/dia/Activity_Diagram_-_Categorise_Entries.dia


./contents/img/Activity_Diagram_-_Manual_Entry.png:
	dia --export=./contents/img/Activity_Diagram_-_Manual_Entry.png --filter=png ../requirements/dia/Activity_Diagram_-_Manual_Entry.dia 

./contents/img/Use_Case_Diagram.png:
	dia --export=./contents/img/Use_Case_Diagram.png --filter=png ../requirements/dia/Use_Case_Diagram.dia 

pdf:	report.tex ./contents/img/Activity_Diagram_-_Categorise_Entries.png ./contents/img/Activity_Diagram_-_Manual_Entry.png ./contents/img/Use_Case_Diagram.png
	pdflatex report && biber report && pdflatex report && pdflatex report

