package com.fullcycle.catalogo.admin.application.castmember.update;

import com.fullcycle.catalogo.admin.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
    extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
    permits DefaultUpdateCastMemberUseCase {
}
