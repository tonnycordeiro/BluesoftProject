<div id="detached" style="background-color:rgb(250,245,201); margin-left:10px; margin-top:20px;">
	<div id="title"><b>Última postagem</b> (tempo para edição: ${standByTime} minutos)</div>
	<form id="formStandByComment" name="formStandByComment" action="/comente-sobre/comment/edit" method="post" onload="decodeFieldsById(['exactTextIntoComment'])">
		<table>
			<tr>
				<td>e-mail:</td>
				<td>
					${standByComment.visitor.email}
				</td>
			</tr>
			<tr>
				<td>comentário:</td>
				<td>
					${standByComment.description}
				</td>
			</tr>
			<tr>
				<td>
					<input type="hidden" name="comment.id" value="${standByComment.id}">
					<button type="submit" name="_method" value="DELETE" >&nbsp;&nbsp;&nbsp;remover&nbsp;&nbsp;&nbsp;</button>
					<input  type="button" value="   editar   " onclick="location.href='/comente-sobre/comment/edit?comment.id=${standByComment.id}'"/>
				</td>
			</tr>						
		</table>
	</form>
</div>
