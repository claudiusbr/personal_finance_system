./contents/img/Comm_Diagram_-_Upload_Statement.png: ../requirements/dia/Comm_Diagram_-_Upload_Statement.dia
	dia --export=./contents/img/Comm_Diagram_-_Upload_Statement.png --filter=png ../requirements/dia/Comm_Diagram_-_Upload_Statement.dia

./contents/img/Comm_Diagram_-_Manual_Entry.png: ../requirements/dia/Comm_Diagram_-_Manual_Entry.dia
	dia --export=./contents/img/Comm_Diagram_-_Manual_Entry.png --filter=png ../requirements/dia/Comm_Diagram_-_Manual_Entry.dia

./contents/img/Comm_Diagram_-_Visualise_Categorised_Summary.png: ../requirements/dia/Comm_Diagram_-_Visualise_Categorised_Summary.dia
	dia --export=./contents/img/Comm_Diagram_-_Visualise_Categorised_Summary.png --filter=png ../requirements/dia/Comm_Diagram_-_Visualise_Categorised_Summary.dia


./contents/img/Class_Diagram_-_Transaction.png: ../requirements/dia/Class_Diagram_-_Transaction.dia
	dia --export=./contents/img/Class_Diagram_-_Transaction.png --filter=png ../requirements/dia/Class_Diagram_-_Transaction.dia

./contents/img/Activity_Diagram_-_Categorise_Entries.png: ../requirements/dia/Activity_Diagram_-_Categorise_Entries.dia
	dia --export=./contents/img/Activity_Diagram_-_Categorise_Entries.png --filter=png ../requirements/dia/Activity_Diagram_-_Categorise_Entries.dia

./contents/img/Class_Diagram_-_Categories_and_Amount.png: ../requirements/dia/Class_Diagram_-_Categories_and_Amount.dia
	dia --export=./contents/img/Class_Diagram_-_Categories_and_Amount.png --filter=png ../requirements/dia/Class_Diagram_-_Categories_and_Amount.dia




./contents/img/Activity_Diagram_-_Manual_Entry.png: ../requirements/dia/Activity_Diagram_-_Manual_Entry.dia 
	dia --export=./contents/img/Activity_Diagram_-_Manual_Entry.png --filter=png ../requirements/dia/Activity_Diagram_-_Manual_Entry.dia 

./contents/img/Use_Case_Diagram.png: ../requirements/dia/Use_Case_Diagram.dia 
	dia --export=./contents/img/Use_Case_Diagram.png --filter=png ../requirements/dia/Use_Case_Diagram.dia 

pdf:	report.tex ./contents/img/Activity_Diagram_-_Categorise_Entries.png ./contents/img/Activity_Diagram_-_Manual_Entry.png ./contents/img/Use_Case_Diagram.png ./contents/img/Class_Diagram_-_Categories_and_Amount.png ./contents/img/Class_Diagram_-_Transaction.png ./contents/img/Comm_Diagram_-_Manual_Entry.png ./contents/img/Comm_Diagram_-_Upload_Statement.png ./contents/img/Comm_Diagram_-_Visualise_Categorised_Summary.png
	pdflatex report && biber report && pdflatex report && pdflatex report && mv report.pdf ../
