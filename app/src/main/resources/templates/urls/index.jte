@import hexlet.code.dto.UrlListPage
@import hexlet.code.model.Url
@param UrlListPage page = null

@template.layout.page(
    content = @`
        <div class="container-lg mt-5">
            @if(page.getUrls().isEmpty())
                <h1>Сайты не добавлены</h1>
            @else
                <h1>Сайты</h1>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                        <tr>
                            <th class="col-1">ID</th>
                            <th>Имя</th>
                            <th class="col-2">Последняя проверка</th>
                            <th class="col-1">Код ответа</th>
                        </tr>
                    </thead>
                    <tbody>
                        @for(Url url : page.getUrls())
                            <tr>
                                <td>
                                    ${url.getId()}
                                </td>
                                <td>
                                    <a href="/urls/${url.getId()}">${url.getName()}</a>
                                </td>
                                <td>
    
                                </td>
                                <td>
    
                                </td>
                            </tr>
                        @endfor
                    </tbody>
                </table>
            @endif
        </div>
    `,
    page = page
)
