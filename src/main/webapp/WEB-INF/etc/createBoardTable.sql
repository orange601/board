CREATE TABLE [dbo].[BOARD](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[SUBJECT] [varchar](300) NOT NULL,
	[CONTENT] [text] NOT NULL,
	[WRITER] [varchar](100) NOT NULL,
	[PASSWORD] [varchar](300) NOT NULL,
	[REGISTER_DATETIME] [datetime] NOT NULL DEFAULT (getdate()),
 CONSTRAINT [PK_BOARD] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]