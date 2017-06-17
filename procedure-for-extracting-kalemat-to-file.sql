-- procedure to extract kalemat el quran to file using sqlserver
-- create c:\makhlouf folder first
-- run CREATE TABLE TEMP(content NVARCHAR(MAX)); 

sp_configure 'xp_cmdshell', 1; 
GO 
RECONFIGURE; 
GO 

DECLARE @MyCursor CURSOR;
DECLARE @Nested CURSOR;
DECLARE @Surah_id INTEGER;
DECLARE @Path  NVARCHAR(2000);
DECLARE @Txt  NVARCHAR(MAX);
DECLARE @Content  NVARCHAR(MAX);
DECLARE @Command NVARCHAR(4000)
BEGIN
    SET @MyCursor = CURSOR FOR
    select _ID from dbo.SURAH;   

    OPEN @MyCursor 
    FETCH NEXT FROM @MyCursor 
    INTO @Surah_id

    WHILE @@FETCH_STATUS = 0
    BEGIN
	  DELETE FROM TEMP;
      SET @Path = CONCAT('C:\makhlouf\', @Surah_id, '.tafsir');
	  SET @Content = '[';

	   SET @Nested = CURSOR FOR
		select CONCAT('{"ayah":"', REPLACE(a.kalemah, '"', ''''), '", "ayahNumber":', a.number, ', "tafsir":"', REPLACE(t.tafsir, '"', ''''), '"}') FROM AYAH a, TAFSIR t 
		WHERE a.SURAH_ID = @Surah_id
		AND a._id = t.AYAH_ID
		ORDER BY a.SURAH_ID, a._id

		OPEN @Nested 
		FETCH NEXT FROM @Nested 
		INTO @Txt

		WHILE @@FETCH_STATUS = 0
		BEGIN
			SET @Content = CONCAT(@Content, @Txt, ',');
			
			FETCH NEXT FROM @Nested 
			INTO @Txt
		END;
		CLOSE @Nested;
		DEALLOCATE @Nested;	

		SET @Content = CONCAT(@Content, ']'); -- close the array
		SET @Content = REPLACE(@Content, ',]', ']'); -- remove last comma after last record
		
		INSERT INTO TEMP VALUES (@Content);

	    SET @Command = 'bcp "SELECT CONTENT FROM TEMP" queryout ' + @Path + ' -c -T -w' 
	    EXEC xp_cmdshell @Command 

	  FETCH NEXT FROM @MyCursor 
      INTO @Surah_id
    END;

    CLOSE @MyCursor ;
    DEALLOCATE @MyCursor;
END;