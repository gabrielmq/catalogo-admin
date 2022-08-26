package com.fullcycle.catalogo.admin.application.castmember.retrive.list;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
    extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
    permits DefaultListCastMembersUseCase {
}
