package fr.sothis.stats.commands.manager;

import fr.sothis.stats.commands.manager.utils.Perms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    String name();
    String usage() default "";
    String description() default "";
    Perms permission() default Perms.ALL;
    Class<? extends SubCommand<?>>[] subCommands() default {};
    boolean autoManagingSubCommands() default true;
    boolean autoCheckPermission() default true;
    boolean hasSubcommandGroups() default false;
    SubCommandDefinition[] subcommandGroups() default {};
}
