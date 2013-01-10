package org.morph.bukget.commands;

/**
 * Every command returns a BukGetCommandResult after execution
 * @author Morphesus
 */
public enum BukGetCommandResult {
    SUCCESS,
    INSUFFICIENT_ARGUMENTS,
    ILLEGAL_ARGUMENT,
    NO_PERMISSION,
    FATAL_ERROR;
}
