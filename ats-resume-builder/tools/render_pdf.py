"""Render a PDF page to PNG so the exported resume layout can be eyeballed."""
import sys

import fitz

pdf_path = sys.argv[1]
png_path = sys.argv[2]
page_index = int(sys.argv[3]) if len(sys.argv) > 3 else 0

doc = fitz.open(pdf_path)
print(f"pages={doc.page_count}")
page = doc[page_index]
page.get_pixmap(dpi=140).save(png_path)
print(f"wrote {png_path}")
