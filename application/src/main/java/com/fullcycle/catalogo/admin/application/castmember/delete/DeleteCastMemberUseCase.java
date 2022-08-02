package com.fullcycle.catalogo.admin.application.castmember.delete;

import com.fullcycle.catalogo.admin.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
    extends UnitUseCase<String>
    permits DefaultDeleteCastMemberUseCase {
}
