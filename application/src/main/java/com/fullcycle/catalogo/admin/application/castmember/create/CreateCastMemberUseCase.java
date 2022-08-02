package com.fullcycle.catalogo.admin.application.castmember.create;

import com.fullcycle.catalogo.admin.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
    extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
    permits DefaultCreateCastMemberUseCase {
}
