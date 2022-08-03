package com.fullcycle.catalogo.admin.application.castmember.retrive.get;

import com.fullcycle.catalogo.admin.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
    extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIdUseCase {
}
