package hudson.plugins.violations.types.perlcritic;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for the Perl::Critic violations type.
 */
public final class PerlCriticDescriptor  extends TypeDescriptor {

    /** The descriptor for the Perl::Critic violations type. */
    public static final String TYPE_NAME = "perlcritic";
    public static final PerlCriticDescriptor DESCRIPTOR = new PerlCriticDescriptor();

    private PerlCriticDescriptor() {
        super(PerlCriticDescriptor.TYPE_NAME);
    }

    /**
     * Create a parser for the Perl::Critic type.
     * @return a new PerlCritic parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new PerlCriticParser();
    }

}

