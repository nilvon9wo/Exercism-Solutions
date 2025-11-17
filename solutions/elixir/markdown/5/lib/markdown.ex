defmodule Markdown do
  @doc """
    Parses a given string with Markdown syntax and returns the associated HTML for that string.

    ## Examples

    iex> Markdown.parse("This is a paragraph")
    "<p>This is a paragraph</p>"

    iex> Markdown.parse("#Header!\n* __Bold Item__\n* _Italic Item_")
    "<h1>Header!</h1><ul><li><em>Bold Item</em></li><li><i>Italic Item</i></li></ul>"
  """
  @header_mark "#"
  @list_item_mark "*"

  @spec parse(String.t()) :: String.t()
  def parse(markdown),
    do:
      markdown
      |> String.split("\n")
      |> Enum.map_join(&to_html/1)
      |> enclose_html_unordered_list()

  # This function assumes headers, lists, and paragraphs will NOT embed each other.
  # Moreover, the syntax of Markdown makes it difficult/impossible to support
  # checking for all Markdown in arbitrary locations and an arbitrary order.
  # So, for example, we don't check for bold and italics here.
  defp to_html(text = @header_mark <> _),
    do: to_html_header(text)

  defp to_html(text = @list_item_mark <> _),
    do: to_html_list_item(text)

  defp to_html(text),
    do: to_html_paragraph(text)

  def to_html_header(header_text),
    do:
      header_text
      |> parse_header_markdown_level()
      |> render_html_header()

  defp parse_header_markdown_level(header) do
    [level_indicator | content] = String.split(header)

    header_tag = "h" <> header_level(level_indicator)
    title = Enum.join(content, " ")
    {header_tag, title}
  end

  defp header_level(head),
    do:
      head
      |> String.length()
      |> to_string()

  defp render_html_header({header_tag, title}),
    do: "<#{header_tag}>#{title}</#{header_tag}>"

  def to_html_list_item(list) do
    tagged_words =
      list
      |> String.trim_leading("* ")
      |> join_words_with_tags()

    "<li>#{tagged_words}</li>"
  end

  # This function assumes:
  # 1. The transformed HTML contains no more than one list; and
  # 2. that the list should be unordered.
  def enclose_html_unordered_list(transformed_html),
    do:
      transformed_html
      |> String.replace("<li>", "<ul><li>", global: false)
      |> String.replace_suffix("</li>", "</li></ul>")

  def to_html_paragraph(word_list) do
    tagged_words = join_words_with_tags(word_list)

    "<p>#{tagged_words}</p>"
  end

  def join_words_with_tags(text),
    do:
      text
      # We need to split the text because otherwise it becomes very
      # complicated to determine which openers should match with which closers.
      |> String.split()
      |> Enum.map_join(_joiner = " ", &decorate_fonts/1)

  defp decorate_fonts(word),
    do:
      word
      |> replace("__", "strong")
      |> replace("_", "em")

  defp replace(word, markdown, html),
    do:
      word
      |> String.replace_leading("#{markdown}", "<#{html}>")
      |> String.replace_trailing("#{markdown}", "</#{html}>")
end
